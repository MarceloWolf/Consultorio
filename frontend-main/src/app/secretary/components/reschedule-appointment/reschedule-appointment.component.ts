import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DayOfWeek } from 'src/app/core/models/businessDays.model';
import { FullMedicalAppointment } from 'src/app/core/models/full-medical-appointment.model';
import { Shift } from 'src/app/core/models/shift.model';

@Component({
  selector: 'app-reschedule-appointment',
  templateUrl: './reschedule-appointment.component.html',
  styleUrls: ['./reschedule-appointment.component.css']
})
export class RescheduleAppointmentComponent implements OnInit {
  @Input() appointment!: FullMedicalAppointment | null;
  @Output() rescheduleConfirmed = new EventEmitter<{ newDate: string; newTime: string }>();
  @Output() closed = new EventEmitter<void>();

  @Input() availableAppointments: Shift[] = [];
  selectedNewAppointment: Shift | null = null;
  availableDays: string[] = [];

  private dayOrder = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];

  ngOnInit(): void {
    this.generateAvailableDays();
  }

  generateAvailableDays(): void {
    let daysSet = new Set<string>();
    this.availableAppointments.forEach(shift => {
      if (shift.businessDays?.dayOfWeek) {
        const dayName = this.getDayName(shift.businessDays.dayOfWeek);
        if (this.dayOrder.includes(dayName)) {
          daysSet.add(dayName);
        }
      }
    });
    this.availableDays = Array.from(daysSet)
      .map(day => day.trim().toUpperCase())  // Elimina espacios y convierte a mayúsculas
      .filter(day => this.dayOrder.includes(day))  // Filtra días válidos
      .sort((a, b) => {
        const aIndex = this.dayOrder.indexOf(a);
        const bIndex = this.dayOrder.indexOf(b);

        console.log(`Comparando: ${a} (índice: ${aIndex}) con ${b} (índice: ${bIndex}), resultado: ${aIndex - bIndex}`);

        return aIndex - bIndex;  // Ordena según el índice en dayOrder
      });
    console.log("Días después de ordenar:", this.availableDays);
  }

  

  getShiftsByDay(dayName: string): Shift[] {
    return this.availableAppointments.filter(shift =>
      shift.businessDays?.dayOfWeek &&
      this.getDayName(shift.businessDays.dayOfWeek) === dayName
    );
  }

  confirmReschedule(): void {
    if (this.selectedNewAppointment && this.selectedNewAppointment.businessDays) {
      const dayName = this.getDayName(this.selectedNewAppointment.businessDays.dayOfWeek);
      const newDate = this.getShiftDate(dayName);
      
      this.rescheduleConfirmed.emit({
        newDate: newDate,
        newTime: this.selectedNewAppointment.shiftTime
      });
      this.closeModal();
    }
  }

  getShiftDate(dayName: string): string {
      const today = new Date();
      const daysOrder = ["Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"];
  
      const targetDayIndex = daysOrder.indexOf(dayName);
      if (targetDayIndex === -1) {
          console.error('Día no válido:', dayName);
          return '';
      }
  
      const todayDayIndex = today.getDay() === 0 ? 6 : today.getDay() - 1;
  
      let daysToAdd = (targetDayIndex - todayDayIndex + 7) % 7;
      if (daysToAdd === 0 && today.getHours() >= 12) {
          daysToAdd = 7;
      }
  
      const targetDate = new Date(today);
      targetDate.setDate(today.getDate() + daysToAdd);
  
      if (targetDate.getDay() === 0) {
          targetDate.setDate(targetDate.getDate() + 1);
      }
  
      return targetDate.toISOString().split('T')[0];
  }

  getDayName(dayEnum: DayOfWeek): string {
    const daysMapping: Record<DayOfWeek, string> = {
      [DayOfWeek.MONDAY]: "Lunes",
      [DayOfWeek.TUESDAY]: "Martes",
      [DayOfWeek.WEDNESDAY]: "Miercoles",
      [DayOfWeek.THURSDAY]: "Jueves",
      [DayOfWeek.FRIDAY]: "Viernes",
      [DayOfWeek.SATURDAY]: "Sabado"
    };
    return daysMapping[dayEnum] || "UNKNOWN";
  }

  closeModal(): void {
    this.closed.emit();
  }
}



