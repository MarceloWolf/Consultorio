import os
from PIL import Image, ImageDraw, ImageFont

# Define directory for saving diagram images
os.makedirs("documentación/análisis funcional/diagramas", exist_ok=True)

try:
    font_title = ImageFont.truetype("arial.ttf", 16)
    font_bold = ImageFont.truetype("arial.ttf", 12)
    font_regular = ImageFont.truetype("arial.ttf", 11)
    font_small = ImageFont.truetype("arial.ttf", 10)
except Exception:
    # Fallback to default if arial.ttf is not found (unlikely on Windows)
    font_title = ImageFont.load_default()
    font_bold = ImageFont.load_default()
    font_regular = ImageFont.load_default()
    font_small = ImageFont.load_default()

# ----------------- 1. E-R DATABASE DIAGRAM -----------------
def generate_er_diagram():
    img = Image.new("RGB", (1000, 750), "#f8fafc")
    draw = ImageDraw.Draw(img)
    
    # Title
    draw.text((30, 20), "Diagrama Entidad-Relacion (E-R) - Base de Datos Consultorio", fill="#0f172a", font=font_title)
    
    # Palette
    header_bg = "#0d9488" # Teal
    box_border = "#0f766e"
    box_bg = "#ffffff"
    text_color = "#1e293b"
    line_color = "#64748b"
    
    def draw_entity(x, y, w, h, title, fields):
        # Header box
        draw.rectangle([x, y, x + w, y + 25], fill=header_bg, outline=box_border)
        # Title text
        draw.text((x + 10, y + 5), title, fill="#ffffff", font=font_bold)
        # Body box
        draw.rectangle([x, y + 25, x + w, y + h], fill=box_bg, outline=box_border)
        # Fields text
        current_y = y + 32
        for f in fields:
            is_pk = "PK" in f
            is_fk = "FK" in f
            if is_pk:
                draw.text((x + 10, current_y), f, fill="#0f766e", font=font_bold)
            elif is_fk:
                draw.text((x + 10, current_y), f, fill="#b45309", font=font_bold)
            else:
                draw.text((x + 10, current_y), f, fill=text_color, font=font_regular)
            current_y += 18

    # Tables
    # USER table
    draw_entity(30, 70, 180, 190, "user", [
        "id : BIGINT (PK)",
        "username : VARCHAR",
        "password : VARCHAR",
        "dni : VARCHAR",
        "name : VARCHAR",
        "lastname : VARCHAR",
        "email : VARCHAR",
        "phone_number : VARCHAR",
        "role : VARCHAR",
        "account_state : VARCHAR",
        "new_account : BOOLEAN"
    ])
    
    # PROFESSIONAL table
    draw_entity(250, 70, 180, 90, "professional", [
        "id : BIGINT (PK, FK)",
        "start_time : TIME",
        "end_time : TIME"
    ])
    
    # SECRETARY table
    draw_entity(250, 180, 180, 60, "secretary", [
        "id : BIGINT (PK, FK)",
    ])
    
    # PATIENT table
    draw_entity(500, 70, 180, 160, "patient", [
        "dni : VARCHAR (PK)",
        "name : VARCHAR",
        "lastname : VARCHAR",
        "email : VARCHAR",
        "phone_number : VARCHAR",
        "address : VARCHAR",
        "birthdate : DATE",
        "active : BOOLEAN"
    ])
    
    # TOOTH_STATE table
    draw_entity(750, 70, 200, 110, "tooth_state", [
        "id : BIGINT (PK)",
        "patient_id : VARCHAR (FK)",
        "tooth_number : INT",
        "state : VARCHAR",
        "notes : TEXT"
    ])
    
    # MEDICAL_IMAGE table
    draw_entity(750, 210, 200, 130, "medical_image", [
        "id : BIGINT (PK)",
        "patient_id : VARCHAR (FK)",
        "file_name : VARCHAR",
        "file_type : VARCHAR",
        "upload_date : DATE",
        "comments : TEXT",
        "image_data : LONGTEXT"
    ])
    
    # MEDICAL_RECORD table
    draw_entity(500, 310, 180, 80, "medical_record", [
        "id : BIGINT (PK)",
        "patient_id : VARCHAR (FK)"
    ])

    # CONSULTATION table
    draw_entity(500, 450, 210, 110, "consultation", [
        "id : BIGINT (PK)",
        "medical_record_id : BIGINT (FK)",
        "professional_id : BIGINT (FK)",
        "reason : VARCHAR",
        "diagnosis : TEXT",
        "treatment : TEXT"
    ])
    
    # SHIFT (TURNO) table
    draw_entity(240, 310, 200, 140, "shift", [
        "id : BIGINT (PK)",
        "date : DATE",
        "time : TIME",
        "patient_id : VARCHAR (FK)",
        "professional_id : BIGINT (FK)",
        "state : VARCHAR"
    ])
    
    # AUDIT_LOG table
    draw_entity(30, 350, 180, 110, "audit_log", [
        "id : BIGINT (PK)",
        "username : VARCHAR",
        "timestamp : DATETIME",
        "action : VARCHAR",
        "details : TEXT"
    ])

    # Draw Relationship Lines
    # user -> professional (1-to-1)
    draw.line([(210, 110), (250, 110)], fill=line_color, width=2)
    # user -> secretary (1-to-1)
    draw.line([(210, 210), (250, 210)], fill=line_color, width=2)
    
    # patient -> tooth_state (1-to-many)
    draw.line([(680, 120), (750, 120)], fill=line_color, width=2)
    draw.line([(740, 115), (750, 120), (740, 125)], fill=line_color, width=2) # crow foot arrow
    
    # patient -> medical_image (1-to-many)
    draw.line([(680, 150), (710, 150), (710, 260), (750, 260)], fill=line_color, width=2)
    draw.line([(740, 255), (750, 260), (740, 265)], fill=line_color, width=2) # crow foot
    
    # patient -> medical_record (1-to-1)
    draw.line([(590, 230), (590, 310)], fill=line_color, width=2)
    
    # medical_record -> consultation (1-to-many)
    draw.line([(590, 390), (590, 450)], fill=line_color, width=2)
    draw.line([(585, 440), (590, 450), (595, 440)], fill=line_color, width=2)
    
    # professional -> consultation (1-to-many)
    draw.line([(340, 160), (340, 500), (500, 500)], fill=line_color, width=2)
    draw.line([(490, 495), (500, 500), (490, 505)], fill=line_color, width=2)
    
    # patient -> shift (1-to-many)
    draw.line([(500, 200), (470, 200), (470, 380), (440, 380)], fill=line_color, width=2)
    
    # professional -> shift (1-to-many)
    draw.line([(340, 160), (320, 160), (320, 310)], fill=line_color, width=2)

    img.save("documentación/análisis funcional/diagramas/er_diagram.png")
    print("ER Diagram generated as PNG.")

# ----------------- 2. USE CASE DIAGRAM -----------------
def generate_use_case_diagram():
    img = Image.new("RGB", (800, 600), "#f8fafc")
    draw = ImageDraw.Draw(img)
    
    # Title
    draw.text((30, 20), "Diagrama de Casos de Uso del Sistema - Centro Odontologico", fill="#0f172a", font=font_title)
    
    # Draw Actors
    def draw_actor(x, y, label):
        # head
        draw.ellipse([x-10, y-30, x+10, y-10], outline="#0f172a", width=2, fill="#ffffff")
        # body
        draw.line([(x, y-10), (x, y+20)], fill="#0f172a", width=2)
        # arms
        draw.line([(x-20, y), (x+20, y)], fill="#0f172a", width=2)
        # legs
        draw.line([(x, y+20), (x-15, y+45)], fill="#0f172a", width=2)
        draw.line([(x, y+20), (x+15, y+45)], fill="#0f172a", width=2)
        # label
        draw.text((x - 30, y + 55), label, fill="#0f172a", font=font_bold)
        
    # Draw Use Cases
    def draw_use_case(x, y, w, h, text):
        draw.ellipse([x, y, x + w, y + h], outline="#0d9488", fill="#ffffff", width=2)
        draw.text((x + 15, y + 15), text, fill="#1e293b", font=font_regular)

    # Place Actors
    draw_actor(80, 150, "Administrador")
    draw_actor(80, 380, "Secretaria")
    draw_actor(720, 180, "Odontologo")
    draw_actor(720, 400, "Paciente")
    
    # System Boundary Box
    draw.rectangle([180, 60, 620, 550], outline="#cbd5e1", width=2)
    draw.text((200, 70), "Sistema Medicus - Control Clinico", fill="#94a3b8", font=font_small)
    
    # Place Use Cases
    draw_use_case(250, 90, 200, 50, "Administrar Usuarios y Roles") # UC1
    draw_use_case(250, 160, 200, 50, "Registrar / Bloquear Turnos") # UC2
    draw_use_case(250, 230, 200, 50, "Ver Agenda Odontologica") # UC3
    draw_use_case(250, 300, 200, 50, "Registrar Consulta Clinica") # UC4
    draw_use_case(250, 370, 200, 50, "Modificar Odontograma FDI") # UC5
    draw_use_case(250, 440, 200, 50, "Cargar y Analizar Placas RX") # UC6
    draw_use_case(250, 490, 200, 50, "Autogestion / Ver Mis Estudios") # UC7
    
    # Connect Actor -> Use Cases
    # Admin links
    draw.line([(100, 150), (250, 115)], fill="#64748b", width=1)
    draw.line([(100, 150), (250, 185)], fill="#64748b", width=1)
    draw.line([(100, 150), (250, 255)], fill="#64748b", width=1)
    draw.line([(100, 150), (250, 325)], fill="#64748b", width=1)
    draw.line([(100, 150), (250, 395)], fill="#64748b", width=1)
    
    # Secretaria links
    draw.line([(100, 380), (250, 185)], fill="#64748b", width=1)
    draw.line([(100, 380), (250, 255)], fill="#64748b", width=1)
    
    # Odontologo links
    draw.line([(700, 180), (450, 255)], fill="#64748b", width=1)
    draw.line([(700, 180), (450, 325)], fill="#64748b", width=1)
    draw.line([(700, 180), (450, 395)], fill="#64748b", width=1)
    draw.line([(700, 180), (450, 465)], fill="#64748b", width=1)
    
    # Paciente links
    draw.line([(700, 400), (450, 515)], fill="#64748b", width=1)

    img.save("documentación/análisis funcional/diagramas/use_case_diagram.png")
    print("Use Case Diagram generated as PNG.")

# ----------------- 3. SEQUENCE DIAGRAM -----------------
def generate_sequence_diagram():
    img = Image.new("RGB", (800, 600), "#f8fafc")
    draw = ImageDraw.Draw(img)
    
    # Title
    draw.text((30, 20), "Diagrama de Secuencia - Actualizacion de Odontograma / Ficha", fill="#0f172a", font=font_title)
    
    # Participants columns
    cols = {
        "prof": 120,
        "ui": 300,
        "guard": 480,
        "back": 660
    }
    
    # Draw Participant Boxes
    def draw_participant(x, name):
        draw.rectangle([x - 50, 60, x + 50, 95], fill="#0d9488", outline="#0f766e", width=2)
        draw.text((x - 40, 72), name, fill="#ffffff", font=font_bold)
        # Lifeline
        draw.line([(x, 95), (x, 540)], fill="#cbd5e1", width=1)
        
    draw_participant(cols["prof"], "Profesional")
    draw_participant(cols["ui"], "Frontend (UI)")
    draw_participant(cols["guard"], "JWT Guard")
    draw_participant(cols["back"], "Backend (API)")
    
    # Timeline steps
    def draw_msg(from_p, to_p, y, text, is_async=False):
        x1 = cols[from_p]
        x2 = cols[to_p]
        # Line
        if is_async:
            # dashed line
            for i in range(min(x1, x2), max(x1, x2), 8):
                draw.line([(i, y), (i + 4, y)], fill="#334155", width=1)
        else:
            draw.line([(x1, y), (x2, y)], fill="#334155", width=2)
            
        # Arrowhead
        arrow_size = 6
        direction = 1 if x2 > x1 else -1
        draw.polygon([(x2, y), (x2 - direction*arrow_size, y - arrow_size), (x2 - direction*arrow_size, y + arrow_size)], fill="#334155")
        # Text label
        label_x = min(x1, x2) + abs(x2 - x1)/2 - 80
        draw.text((label_x, y - 14), text, fill="#0f172a", font=font_small)

    # 1. Click Tooth
    draw_msg("prof", "ui", 140, "1. Click Diente (Nomenclature FDI)")
    # 2. Show Edit Modal
    draw_msg("ui", "prof", 180, "2. Muestra Modal con Formulario", is_async=True)
    
    # Action bar activation lines (thick boxes)
    draw.rectangle([cols["ui"] - 6, 140, cols["ui"] + 6, 280], fill="#e2e8f0", outline="#64748b")
    draw.rectangle([cols["back"] - 6, 260, cols["back"] + 6, 440], fill="#cbd5e1", outline="#475569")
    
    # 3. Select state & save
    draw_msg("prof", "ui", 220, "3. Guarda Cambios (Estado/Notas)")
    
    # 4. Send POST request
    draw_msg("ui", "guard", 260, "4. POST /api/tooth-state (HTTP)")
    # 5. Token validation
    draw_msg("guard", "back", 300, "5. Valida JWT y Claims")
    
    # 6. Database save and audit logging
    draw_msg("back", "back", 350, "6. DB Save & AuditLog Insercion")
    draw.arc([cols["back"], 340, cols["back"] + 40, 370], 270, 90, fill="#334155", width=2)
    
    # 7. HTTP Response 200 OK
    draw_msg("back", "ui", 420, "7. HTTP 200 OK (Exito)", is_async=True)
    
    # 8. Render Visual Update
    draw_msg("ui", "prof", 460, "8. Actualiza Mapa FDI y Alerta", is_async=True)

    img.save("documentación/análisis funcional/diagramas/sequence_diagram.png")
    print("Sequence Diagram generated as PNG.")

if __name__ == "__main__":
    generate_er_diagram()
    generate_use_case_diagram()
    generate_sequence_diagram()
    print("All diagram images created successfully.")
