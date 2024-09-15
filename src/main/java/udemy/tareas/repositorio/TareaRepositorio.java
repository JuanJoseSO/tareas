package udemy.tareas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udemy.tareas.modelo.Tarea;

@Repository
public interface TareaRepositorio extends JpaRepository<Tarea,Integer> {}
