export interface User {
    id?: number; 
    dni: string; 
    name: string;
    lastname: string;
    address: string;
    email: string; 
    phoneNumber: string; 
    username: string; 
    password: string;
    role: RoleEnum; 
    newAccount: boolean; 
    accountState: AccountStateEnum; 
}

export enum AccountStateEnum {
    ACTIVE = "ACTIVE",
    INACTIVE = "INACTIVE"
}

export enum RoleEnum {
    ADMIN = "ADMIN",
    SECRETARY = "SECRETARY",
    PROFESSIONAL = "PROFESSIONAL"
}