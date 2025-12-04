package dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class DAO<E> {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Class<E> classe;

    private static synchronized EntityManagerFactory getEmf() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("MammaMia");
                System.out.println("✅ EntityManagerFactory criado com sucesso!");
            } catch (Exception e) {
                System.err.println("❌ ERRO: Não foi possível conectar ao banco");
                System.err.println("💡 A aplicação continuará em modo offline");
                emf = null;
            }
        }
        return emf;
    }

    public DAO(Class<E> classe) {
        this.classe = classe;
        try {
            EntityManagerFactory factory = getEmf();
            if (factory != null) {
                this.em = factory.createEntityManager();
            } else {
                this.em = null;
                System.err.println("⚠️  Modo offline - Sem conexão com banco");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar EntityManager: " + e.getMessage());
            this.em = null;
        }
    }

    public DAO() {
        this(null);
    }

    // 🔥 CORREÇÃO: Método verificarConexao não lança exceção
    private boolean verificarConexao() {
        if (em == null) {
            System.err.println("⚠️  Operação cancelada - Sem conexão com banco");
            return false;
        }
        return true;
    }

    // ======================= MÉTODOS DE TRANSAÇÃO =======================
    public DAO<E> abrirTransacao() {
        if (!verificarConexao()) return this;
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        return this;
    }

    public DAO<E> fecharTransacao() {
        if (!verificarConexao()) return this;
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        return this;
    }

    public DAO<E> incluirE(E entidade) {
        if (!verificarConexao()) return this;
        em.persist(entidade);
        return this;
    }

    public DAO<E> incluirTransacional(E entidade) {
        if (!verificarConexao()) return this;
        try {
            return this.abrirTransacao().incluirE(entidade).fecharTransacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao incluir: " + e.getMessage());
            return this;
        }
    }

    // ======================= MÉTODOS DE CONSULTA =======================
    public List<E> obterTodos(int quantidade, int deslocamento) {
        if (!verificarConexao()) return java.util.Collections.emptyList();
        try {
            String jpql = "select e from " + classe.getName() + " e";
            TypedQuery<E> query = em.createQuery(jpql, classe);
            query.setMaxResults(quantidade);
            query.setFirstResult(deslocamento);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter todos: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public E obterPorID(Object id) {
        if (!verificarConexao()) return null;
        try {
            return em.find(classe, id);
        } catch (Exception e) {
            System.err.println("❌ Erro ao obter por ID: " + e.getMessage());
            return null;
        }
    }

    // ======================= MÉTODOS DE EXCLUSÃO =======================
    private DAO<E> removerPorId(Long id) {
        if (!verificarConexao()) return this;
        try {
            E entidade = em.find(classe, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            return this;
        } catch (Exception e) {
            System.err.println("❌ Erro ao remover por ID: " + e.getMessage());
            return this;
        }
    }

    public DAO<E> removerPorIdTransacional(Long id) {
        if (!verificarConexao()) return this;
        try {
            return this.abrirTransacao().removerPorId(id).fecharTransacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao remover transacional: " + e.getMessage());
            return this;
        }
    }

    // ======================= MÉTODOS DE ATUALIZAÇÃO =======================
    private DAO<E> atualizar(E entidade) {
        if (!verificarConexao()) return this;
        try {
            em.merge(entidade);
            return this;
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar: " + e.getMessage());
            return this;
        }
    }

    public DAO<E> atualizarTransacional(E entidade) {
        if (!verificarConexao()) return this;
        try {
            return this.abrirTransacao().atualizar(entidade).fecharTransacao();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao atualizar transacional: " + e.getMessage());
            return this;
        }
    }

    // ======================= MÉTODOS DE CONSULTA AVANÇADA =======================
    public List<E> consultar(String jpql, Object parametro) {
        if (!verificarConexao()) return java.util.Collections.emptyList();
        try {
            TypedQuery<E> query = em.createQuery(jpql, classe);
            query.setParameter(1, parametro);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro na consulta: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public List<E> consultar(String jpql, Object... parametros) {
        if (!verificarConexao()) return java.util.Collections.emptyList();
        try {
            TypedQuery<E> query = em.createQuery(jpql, classe);
            for (int i = 0; i < parametros.length; i++) {
                query.setParameter(i + 1, parametros[i]);
            }
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro na consulta múltipla: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    // ======================= MÉTODOS DE CONTAGEM =======================
    public long contar() {
        if (!verificarConexao()) return 0;
        try {
            String jpql = "select count(e) from " + classe.getName() + " e";
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            return q.getSingleResult();
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar: " + e.getMessage());
            return 0;
        }
    }

    // ======================= MÉTODOS DE PAGINAÇÃO =======================
    public List<E> obterPaginaOrdenada(int pagina, int tamanho, String orderBy) {
        if (!verificarConexao()) return java.util.Collections.emptyList();
        try {
            String jpql = "select e from " + classe.getName() + " e order by e." + orderBy;
            TypedQuery<E> query = em.createQuery(jpql, classe);
            query.setFirstResult(pagina * tamanho);
            query.setMaxResults(tamanho);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro na paginação: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    // ======================= MÉTODOS BÁSICOS =======================
    public List<E> listarTodos() {
        if (!verificarConexao()) return java.util.Collections.emptyList();
        try {
            String jpql = "select e from " + classe.getName() + " e";
            TypedQuery<E> query = em.createQuery(jpql, classe);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar todos: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public void fecharEm() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public static void fecharEmf() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
            System.out.println("✅ EntityManagerFactory fechado");
        }
    }
}