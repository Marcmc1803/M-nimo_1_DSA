

package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement
    public class Lector {
        String id;
        String nombre;
        String apellidos;
        String dni;
        String fechaNacimiento;
        String lugarNacimiento;
        String direccion;


        public Lector() {}

        public Lector(String id, String nombre, String apellidos, String dni) {
            this.id = id;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }
        public String getDni() { return dni; }
        public void setDni(String dni) { this.dni = dni; }
        public String getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        public String getLugarNacimiento() { return lugarNacimiento; }
        public void setLugarNacimiento(String lugarNacimiento) { this.lugarNacimiento = lugarNacimiento; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
}
