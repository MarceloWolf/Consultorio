import { User } from './user.model';

export interface Secretary extends User {
    start: string; // Para representar LocalTime en formato ISO
    end: string; // Para representar LocalTime en formato ISO
}