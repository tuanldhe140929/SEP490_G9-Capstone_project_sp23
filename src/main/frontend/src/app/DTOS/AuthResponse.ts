export class AuthResponse {
  email: string;
  accessToken: string;
  role: string;
  username: String;
  constructor() {
    this.email = "";
    this.accessToken = "";
    this.role = "";
    this.username = "";
  }
}
