import { Professional } from './professional.model';
import { Shift } from './shift.model';

export interface BusinessDays {
    id?: number;
    dayOfWeek: DayOfWeek; 
    professional: Professional;
    shifts: Shift[];
}

export enum DayOfWeek {
    MONDAY = "MONDAY",
    TUESDAY = "TUESDAY",
    WEDNESDAY = "WEDNESDAY",
    THURSDAY = "THURSDAY",
    FRIDAY = "FRIDAY",
    SATURDAY = "SATURDAY"
}
