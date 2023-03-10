export class AuthResponse {
  email: string;
  accessToken: string;
  roles: string[];
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.roles = [];
  }
}
