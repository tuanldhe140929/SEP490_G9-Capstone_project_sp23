export class AuthResponse {
  email: string;
  accessToken: string;
  role: string;
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.role = "";
  }
}
