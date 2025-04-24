package challenge.fiap.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface _CrudRepo<T> {
    /**
     * Adiciona um novo objeto no repositório.
     *
     * @param object O objeto a ser adicionado.
     */
    void add(T object);

    /**
     * Retorna a lista de objetos ativos no sistema.
     *
     * @return Lista de objetos.
     */
    List<T> get();

    /**
     * Busca um objeto específico pelo ID no sistema.
     *
     * @param id UUID para encontrar o objeto.
     * @return Optional com o objeto correspondente, ou vazio se não encontrado.
     */
    Optional<T> getById(UUID id);

    /**
     * Busca um objeto pelo ID e atualiza com o novo objeto fornecido.
     *
     * @param id UUID para encontrar o objeto.
     * @param object O novo objeto com os dados atualizados.
     */
    void updateById(UUID id, T object);

    /**
     * Remove logicamente um objeto com base no seu ID.
     *
     * @param id UUID do objeto a ser removido.
     */
    void removeById(UUID id);

    // Admins

    /**
     * Retorna todos os objetos, incluindo os removidos logicamente.
     *
     * @return Lista completa de objetos.
     */
    List<T> getAll();

    /**
     * Retorna um objeto pelo ID, mesmo que esteja removido logicamente.
     *
     * @param id UUID para encontrar o objeto.
     * @return Optional com o objeto correspondente, ou vazio se não encontrado.
     */
    Optional<T> getByIdAdmin(UUID id);

    /**
     * Remove permanentemente um objeto com base no seu ID.
     *
     * @param id UUID do objeto a ser deletado permanentemente.
     */
    void deleteById(UUID id);

    // Redundância

    /**
     * Remove logicamente o objeto fornecido.
     *
     * @param object O objeto a ser removido.
     */
    void remove(T object);

    /**
     * Remove permanentemente o objeto fornecido.
     *
     * @param object O objeto a ser deletado.
     */
    void delete(T object);

    /**
     * Busca um objeto e atualiza com o novo objeto fornecido.
     *
     * @param oldObject Objeto antigo para atualizar.
     * @param newObject Objeto novo para atualizar.
     */
    void update(T oldObject, T newObject);
}
