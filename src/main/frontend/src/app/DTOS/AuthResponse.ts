import { User } from "./User";

export class AuthResponse {
  email: string;
  accessToken: string;
  roles: string[];
  user: User;
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.roles = [];
    this.user = new User();
  }
}
