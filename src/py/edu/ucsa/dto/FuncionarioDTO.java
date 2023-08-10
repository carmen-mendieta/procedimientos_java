package py.edu.ucsa.dto;

public class FuncionarioDTO {

	private Long id;
	private Integer legajo;
	private String nombre;
	private String apellido;
	private Integer cedula;
	private String celular;
	private String email;
	private String direccion;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getLegajo() {
		return legajo;
	}
	public void setLegajo(Integer legajo) {
		this.legajo = legajo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Integer getCedula() {
		return cedula;
	}
	public void setCedula(Integer cedula) {
		this.cedula = cedula;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	@Override
	public String toString() {
		return "Funcionario [legajo=" + legajo + ", nombre=" + nombre + ", apellido=" + apellido + ", cedula=" + cedula
				+ ", celular=" + celular + ", email=" + email + ", direccion=" + direccion + "]";
	}
}
