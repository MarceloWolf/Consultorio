import os
from fpdf import FPDF

def save_pdf(pdf, filepath):
    try:
        pdf.output(filepath)
        print(f"PDF generado: {filepath}")
    except PermissionError:
        print(f"\n[AVISO DE CRIS] ERROR DE PERMISOS: No se pudo guardar '{filepath}' porque esta abierto en su lector de PDF.")
        print("Por favor, cierre el documento PDF en su lector e intente ejecutar el script nuevamente.\n")
    except Exception as e:
        print(f"Error al guardar '{filepath}': {e}")


# Create folder structure
folders = [
    "documentación/análisis funcional/historias_de_usuario/epica_usuarios",
    "documentación/análisis funcional/historias_de_usuario/epica_turnos",
    "documentación/análisis funcional/historias_de_usuario/epica_historial_clinico",
    "documentación/análisis funcional/historias_de_usuario/epica_odontograma_imagenes",
    "documentación/análisis funcional/historias_de_usuario/epica_portal_paciente",
    "documentación/análisis funcional/diagramas",
    "documentación/Pruebas"
]

for folder in folders:
    os.makedirs(folder, exist_ok=True)
    print(f"Directorio verificado/creado: {folder}")


class ProfessionalPDF(FPDF):
    def header(self):
        # Arial bold 15
        self.set_font('helvetica', 'B', 15)
        # Title
        self.cell(0, 10, 'Centro Odontologico Dr. Pablo Wolf - Documentacion Funcional', border=0, ln=1, align='C')
        # Line break
        self.ln(5)

    def footer(self):
        # Position at 1.5 cm from bottom
        self.set_y(-15)
        # Arial italic 8
        self.set_font('helvetica', 'I', 8)
        # Page number
        self.cell(0, 10, f'Pagina {self.page_no()}/{{nb}} - Confidencial / Clinica Dental', 0, 0, 'C')

    def add_section_title(self, label):
        self.set_font('helvetica', 'B', 12)
        self.set_text_color(11, 15, 25) # Navy color
        self.cell(0, 10, label, ln=1, align='L')
        self.ln(2)

    def add_body_text(self, text):
        self.set_font('helvetica', '', 10)
        self.set_text_color(50, 50, 50)
        self.multi_cell(0, 6, text)
        self.ln(4)

    def add_hu_block(self, title, description, criteria):
        self.set_font('helvetica', 'B', 11)
        self.set_text_color(16, 124, 65) # Dark Teal
        self.cell(0, 8, title, ln=1, align='L')
        self.set_font('helvetica', 'B', 10)
        self.set_text_color(30, 30, 30)
        self.cell(0, 6, "Descripcion:", ln=1)
        self.add_body_text(description)
        self.set_font('helvetica', 'B', 10)
        self.set_text_color(30, 30, 30)
        self.cell(0, 6, "Criterios de Aceptacion:", ln=1)
        self.add_body_text(criteria)
        self.ln(2)

# --- 1. GENERAL ANALISIS FUNCIONAL OVERVIEW ---
pdf = ProfessionalPDF()
pdf.alias_nb_pages()
pdf.add_page()
pdf.add_section_title("1. Introduccion y Arquitectura del Sistema")
pdf.add_body_text(
    "El presente documento detalla la especificacion del sistema para el Consultorio Odontologico "
    "del Dr. Pablo Wolf. El sistema se construye bajo una arquitectura robusta compuesta por un backend "
    "desarrollado en Java (Spring Boot) con base de datos relacional MySQL y persistencia gestionada mediante "
    "Spring Data JPA, y un frontend web responsivo desarrollado en Angular.\n\n"
    "El objetivo es maximizar la eficiencia en la administracion de turnos, historiales clinicos, "
    "tratamientos dentales detallados por pieza (Odontograma interactivo) y visualizacion de estudios radiologicos (RX y TAC), "
    "respetando de forma estricta las normativas vigentes sobre proteccion de datos personales sensibles (Ley 25.326)."
)
pdf.add_section_title("2. Marco Regulatorio y Seguridad (Ley 25.326)")
pdf.add_body_text(
    "De acuerdo con la legislacion de Proteccion de Datos Personales (Habeas Data), toda la informacion medica "
    "y los diagnosticos se consideran datos sensibles y requieren el mas alto estandar de confidencialidad y control:\n"
    "- Trazabilidad Absoluta: Cada accion de insercion o modificacion de historias clinicas, odontogramas o imagenes medicas "
    "debe registrarse en una tabla de auditoria ('audit_log') con DNI de usuario, timestamp y detalle de la operacion.\n"
    "- Control de Accesos: El portal de pacientes restringe la visibilidad unicamente a los datos del propio paciente. "
    "Los perfiles Profesional y Secretaria poseen accesos segmentados por sus roles funcionales correspondientes."
)
save_pdf(pdf, "documentación/análisis funcional/analisis_funcional.pdf")

# --- 2. EPICA USUARIOS ---
pdf_u = ProfessionalPDF()
pdf_u.alias_nb_pages()
pdf_u.add_page()
pdf_u.add_section_title("Epica: Administracion de Usuarios y Roles")
pdf_u.add_hu_block(
    "HU-101: Autenticacion con JWT y Control de Sesion",
    "Como usuario del sistema (Admin, Profesional, Secretaria, Paciente),\n"
    "quiero ingresar credenciales validas en una pantalla de login premium con fondo translucido y logo vectorial SVG,\n"
    "para acceder de forma segura a las funciones correspondientes a mi rol.",
    "- El backend emite un token JWT firmado conteniendo las claims de rol de usuario.\n"
    "- El frontend guarda el token de manera segura y lo incluye en la cabecera 'Authorization'.\n"
    "- Se valida que un usuario no pueda saltar rutas sin token valido mediante Angular Guards."
)
pdf_u.add_hu_block(
    "HU-102: Cambio de Tema Global (Modo Claro / Modo Oscuro)",
    "Como usuario del sistema,\n"
    "quiero un boton toggle visible en la cabecera para cambiar el esquema visual del sitio,\n"
    "para adaptar el uso de la aplicacion en condiciones de alta y baja luminosidad de manera confortable.",
    "- Al activar el switch, la aplicacion cambia dinamicamente las variables CSS de root.\n"
    "- El estado seleccionado se guarda en LocalStorage para recordar la preferencia del usuario en futuras sesiones.\n"
    "- Las transiciones de color de fondos y tarjetas deben ser fluidas (0.3s ease)."
)
save_pdf(pdf_u, "documentación/análisis funcional/historias_de_usuario/epica_usuarios/hu_usuarios.pdf")

# --- 3. EPICA TURNOS ---
pdf_t = ProfessionalPDF()
pdf_t.alias_nb_pages()
pdf_t.add_page()
pdf_t.add_section_title("Epica: Gestion Integrada de Turnos (Shifts & Appointments)")
pdf_t.add_hu_block(
    "HU-201: Asignacion y Bloqueo de Turnos por Secretaria",
    "Como Secretaria,\n"
    "quiero visualizar la agenda de turnos en un calendario y asignar citas a pacientes,\n"
    "para evitar superposiciones y gestionar la disponibilidad medica eficientemente.",
    "- No se permite registrar dos turnos en el mismo rango horario para el mismo odontologo.\n"
    "- La secretaria puede filtrar la agenda por especialidad y odontologo.\n"
    "- La aplicacion provee estados visuales claros (Pendiente, Realizado, Cancelado)."
)
save_pdf(pdf_t, "documentación/análisis funcional/historias_de_usuario/epica_turnos/hu_turnos.pdf")

# --- 4. EPICA HISTORIAL CLINICO ---
pdf_h = ProfessionalPDF()
pdf_h.alias_nb_pages()
pdf_h.add_page()
pdf_h.add_section_title("Epica: Ficha Medica e Historial Clinico (Medical Records)")
pdf_h.add_hu_block(
    "HU-301: Registro de Consultas Medicas",
    "Como Odontologo (Profesional),\n"
    "quiero registrar una nueva consulta medica detallando el diagnostico, motivo y tratamiento,\n"
    "para mantener un registro cronologico y legal del estado del paciente.",
    "- Solo los usuarios con rol 'PROFESSIONAL' o 'ADMIN' pueden agregar y modificar consultas en la ficha.\n"
    "- El sistema asocia automaticamente el profesional logueado y la fecha/hora actual del servidor.\n"
    "- Se disparan logs de auditoria correspondientes en la base de datos tras confirmar el registro."
)
save_pdf(pdf_h, "documentación/análisis funcional/historias_de_usuario/epica_historial_clinico/hu_historial.pdf")

# --- 5. EPICA ODONTOGRAMA E IMAGENES (NUEVA EXCLUSIVA) ---
pdf_o = ProfessionalPDF()
pdf_o.alias_nb_pages()
pdf_o.add_page()
pdf_o.add_section_title("Epica: Odontograma Interactivo y Visor de Imagenes RX / TAC")
pdf_o.add_hu_block(
    "HU-401: Mapa Bucal Interactivo (Odontograma)",
    "Como Odontologo (Profesional),\n"
    "quiero un mapa bucal interactivo SVG representando las 32 piezas dentales adultas,\n"
    "donde pueda hacer click sobre cualquier diente para cambiar su estado (Sano, Caries, Conducto, Corona, Ausente)\n"
    "y guardar este mapa personalizado para cada paciente.",
    "- Se muestra un diagrama SVG interactivo con las 32 piezas codificadas segun la nomenclatura FDI.\n"
    "- Cada cara del diente (o el diente completo) se pinta segun el estado seleccionado:\n"
    "  * Sano: Verde / Transparente\n"
    "  * Caries: Rojo\n"
    "  * Conducto: Azul\n"
    "  * Corona: Amarillo / Naranja\n"
    "  * Ausente: Gris\n"
    "- El estado del odontograma se persiste en la base de datos vinculado al DNI del paciente.\n"
    "- Al cargar la ficha del paciente, el odontograma muestra el estado actual guardado."
)
pdf_o.add_hu_block(
    "HU-402: Visor de Imagenes Radiograficas (RX) y Tomografias (TAC)",
    "Como Odontologo (Profesional),\n"
    "quiero una herramienta de carga y visualizacion de placas RX o cortes tomograficos (TAC),\n"
    "con controles de ajuste (brillo, contraste, inversion de colores, zoom y paneo),\n"
    "para analizar mejor los estudios desde la ficha del paciente.",
    "- El profesional puede cargar imagenes en formato PNG/JPG/WEBP clasificandolas como RX o TAC.\n"
    "- El visor cuenta con barras deslizantes (sliders) para ajustar brillo (-100 a +100) y contraste (-100 a +100) en tiempo real.\n"
    "- Cuenta con un boton para invertir colores (efecto negativo de radiografia tradicional).\n"
    "- Permite hacer zoom interactivo (acercar/alejar) mediante botones dedicados o rueda del raton.\n"
    "- Las imagenes se guardan en el servidor (Base64 LONGTEXT) vinculadas a la historia clinica del paciente."
)
save_pdf(pdf_o, "documentación/análisis funcional/historias_de_usuario/epica_odontograma_imagenes/hu_odontograma.pdf")

# --- 6. EPICA PORTAL PACIENTE ---
pdf_p = ProfessionalPDF()
pdf_p.alias_nb_pages()
pdf_p.add_page()
pdf_p.add_section_title("Epica: Portal del Paciente (Patient Self-Service)")
pdf_p.add_hu_block(
    "HU-501: Autogestion del Paciente",
    "Como Paciente,\n"
    "quiero ingresar a una vista de portal web dedicada y amigable,\n"
    "para ver el historial de mis turnos proximos y pasados, visualizar mis diagnosticos y descargar mi ficha odontologica basica.",
    "- El paciente accede con su DNI como usuario y una contraseña segura.\n"
    "- El sistema bloquea cualquier solicitud a DNI que no coincida con el token de sesion del paciente.\n"
    "- Muestra un diseño premium de Marian en 'src/app/patient' con mocks responsivos."
)
save_pdf(pdf_p, "documentación/análisis funcional/historias_de_usuario/epica_portal_paciente/hu_portal.pdf")

# --- 7. DIAGRAMAS Y ESQUEMAS ---
pdf_d = ProfessionalPDF()
pdf_d.alias_nb_pages()

# Page 1: E-R Diagram
pdf_d.add_page()
pdf_d.add_section_title("1. Diagrama Entidad-Relacion (E-R) de la Base de Datos")
pdf_d.add_body_text(
    "El siguiente diagrama ilustra el modelado fisico de datos. Se detallan las tablas principales del sistema "
    "(user, patient, professional, secretary, tooth_state, medical_image, medical_record, consultation, shift, audit_log) "
    "junto con sus claves primarias (PK), claves foraneas (FK) y sus relaciones logicas:"
)
pdf_d.ln(2)
pdf_d.image("documentación/análisis funcional/diagramas/er_diagram.png", x=15, y=60, w=180)

# Page 2: Use Case Diagram
pdf_d.add_page()
pdf_d.add_section_title("2. Diagrama de Casos de Uso del Sistema")
pdf_d.add_body_text(
    "El diagrama de Casos de Uso expone las interacciones de los distintos actores "
    "(Administrador, Secretaria, Odontologo y Paciente) con los modulos principales de la aplicacion "
    "dentro de los limites del sistema:"
)
pdf_d.ln(2)
pdf_d.image("documentación/análisis funcional/diagramas/use_case_diagram.png", x=15, y=60, w=180)

# Page 3: Sequence Diagram
pdf_d.add_page()
pdf_d.add_section_title("3. Diagrama de Secuencia: Guardar Estado del Odontograma")
pdf_d.add_body_text(
    "El diagrama de secuencia describe el flujo de eventos y mensajes que ocurren entre "
    "los componentes del sistema (Frontend UI, JWT Guard, Backend API y Base de Datos) "
    "cuando un Profesional actualiza una pieza del Odontograma:"
)
pdf_d.ln(2)
pdf_d.image("documentación/análisis funcional/diagramas/sequence_diagram.png", x=15, y=60, w=180)

save_pdf(pdf_d, "documentación/análisis funcional/diagramas/diagramas.pdf")

print("Generacion de toda la documentacion finalizada exitosamente.")
