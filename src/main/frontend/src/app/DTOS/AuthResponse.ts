export class AuthResponse {
  email: string;
  accessToken: string;
  username: string;
  roles: string[];
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.roles = [];
    this.username = "";
  }
}
