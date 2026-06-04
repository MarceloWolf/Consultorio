import { BusinessDays } from './businessDays.model';

export interface Shift {
    id?: number; 
    shiftTime: string; 
    shiftDate: string;
    isShiftReserved: boolean;
    businessDays: BusinessDays;
}