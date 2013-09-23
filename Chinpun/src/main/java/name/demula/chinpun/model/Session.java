package name.demula.chinpun.model;

import java.util.Calendar;

/**
 * Created by Jesus on 12/08/13.
 */
public class Session {
  private long idFichaje;
  private User persona;
  private Calendar fechaInicio;
  private Calendar fechaFin;
  private Calendar horas;
  private boolean horasExtras;
  private String observaciones;
  private User usuarioInserta;
  private Calendar fechaInsercion;
  private Project proyecto;
  private Task tarea;

  public Session() {
  }

  public long getIdFichaje() {
    return idFichaje;
  }

  public void setIdFichaje(long idFichaje) {
    this.idFichaje = idFichaje;
  }

  public User getPersona() {
    return persona;
  }

  public void setPersona(User persona) {
    this.persona = persona;
  }

  public Calendar getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(Calendar fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public Calendar getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(Calendar fechaFin) {
    this.fechaFin = fechaFin;
  }

  public Calendar getHoras() {
    return horas;
  }

  public void setHoras(Calendar horas) {
    this.horas = horas;
  }

  public boolean isHorasExtras() {
    return horasExtras;
  }

  public void setHorasExtras(boolean horasExtras) {
    this.horasExtras = horasExtras;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public User getUsuarioInserta() {
    return usuarioInserta;
  }

  public void setUsuarioInserta(User usuarioInserta) {
    this.usuarioInserta = usuarioInserta;
  }

  public Calendar getFechaInsercion() {
    return fechaInsercion;
  }

  public void setFechaInsercion(Calendar fechaInsercion) {
    this.fechaInsercion = fechaInsercion;
  }

  public Project getProyecto() {
    return proyecto;
  }

  public void setProyecto(Project proyecto) {
    this.proyecto = proyecto;
  }

  public Task getTarea() {
    return tarea;
  }

  public void setTarea(Task tarea) {
    this.tarea = tarea;
  }


}
