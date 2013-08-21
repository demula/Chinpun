package name.demula.chinpun.model;

/**
 * Created by Jesus on 12/08/13.
 */
public class TaskState {
    private long idEstadoTarea;
    private String descripcion;

    public TaskState() {
    }

    public long getIdEstadoTarea() {
        return idEstadoTarea;
    }

    public void setIdEstadoTarea(long idEstadoTarea) {
        this.idEstadoTarea = idEstadoTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
