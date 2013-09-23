package name.demula.chinpun.model;

import java.util.Calendar;

/**
 * Created by Jesus on 12/08/13.
 */
public class User {
  private long idPersona;
  private String nombre;
  private String apellido1;
  private String apellido2;
  private String nombreCompleto;
  private String idLdap;
  private String estado;
  private Calendar fechaNacimiento;
  private Calendar fechaAlta;

  public User() {
  }

  public long getIdPersona() {
    return idPersona;
  }

  public void setIdPersona(long idPersona) {
    this.idPersona = idPersona;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido1() {
    return apellido1;
  }

  public void setApellido1(String apellido1) {
    this.apellido1 = apellido1;
  }

  public String getApellido2() {
    return apellido2;
  }

  public void setApellido2(String apellido2) {
    this.apellido2 = apellido2;
  }

  public String getNombreCompleto() {
    return nombreCompleto;
  }

  public void setNombreCompleto(String nombreCompleto) {
    this.nombreCompleto = nombreCompleto;
  }

  public String getIdLdap() {
    return idLdap;
  }

  public void setIdLdap(String idLdap) {
    this.idLdap = idLdap;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public Calendar getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(Calendar fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public Calendar getFechaAlta() {
    return fechaAlta;
  }

  public void setFechaAlta(Calendar fechaAlta) {
    this.fechaAlta = fechaAlta;
  }
}
