export class AuthResponse {
  email: string;
  accessToken: string;
  username: string;
  role: string;
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.role = "";
    this.username = "";
  }
}
