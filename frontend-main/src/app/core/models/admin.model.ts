import { AdminUser } from "./adminUser.model";

export interface Admin {
    users: AdminUser[];
    addUser(userName: string, password: string): boolean;
}