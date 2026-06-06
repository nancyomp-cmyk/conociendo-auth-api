package com.conociendo.authapi.repository;

// =====================================================
// Archivo: UsuarioRepository.java
// Descripción: Interfaz para acceder a la tabla usuarios en MySQL
// Spring Data JPA genera automáticamente los métodos de consulta
// =====================================================

import com.conociendo.authapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario.
 *
 * Al extender JpaRepository, Spring genera automáticamente:
 * - save() → guardar un usuario
 * - findById() → buscar por ID
 * - findAll() → listar todos
 * - delete() → eliminar
 * entre otros.
 *
 * Los métodos personalizados se definen con nombre descriptivo
 * y Spring genera la consulta SQL automáticamente.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su dirección de email.
     * Spring genera: SELECT * FROM usuarios WHERE email = ?
     *
     * Se usa en el login para encontrar al usuario.
     *
     * @param email correo electrónico a buscar
     * @return Optional con el usuario si existe, vacío si no existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si ya existe un usuario con ese email.
     * Spring genera: SELECT COUNT(*) > 0 FROM usuarios WHERE email = ?
     *
     * Se usa en el registro para evitar emails duplicados.
     *
     * @param email correo electrónico a verificar
     * @return true si ya existe un usuario con ese email
     */
    boolean existsByEmail(String email);
}