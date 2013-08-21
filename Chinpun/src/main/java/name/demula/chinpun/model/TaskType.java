package name.demula.chinpun.model;

/**
 * Created by Jesus on 12/08/13.
 */
public class TaskType {
    private long idTipoTarea;
    private String nombre;

    public TaskType() {
    }

    public long getIdTipoTarea() {
        return idTipoTarea;
    }

    public void setIdTipoTarea(long idTipoTarea) {
        this.idTipoTarea = idTipoTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
