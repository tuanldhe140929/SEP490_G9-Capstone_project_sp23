import { Injectable } from '@angular/core';
import { AuthResponse } from '../interfaces/AuthResponse';




const AUTH_RESPONSE_KEY = 'auth-user';
const TOKEN_KEY = 'auth-token';
@Injectable({
  providedIn: 'root'
})
export class StorageService {
  constructor() {}


	signOut(): void {
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }
  public saveUser(authResponse: AuthResponse): void {
    window.sessionStorage.removeItem(AUTH_RESPONSE_KEY);
    window.sessionStorage.setItem(AUTH_RESPONSE_KEY, JSON.stringify(authResponse));
  }

  public getAuthResponse(): any {
    const authResponseJson = window.sessionStorage.getItem(AUTH_RESPONSE_KEY);
    let authResponse:AuthResponse;
    if (authResponseJson) {
       let authResponse:AuthResponse =  JSON.parse(authResponseJson);
       return authResponse;
    }
    return "";
    
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(AUTH_RESPONSE_KEY);
    if (user) {
      return true;
    }
    return false;
  }
}