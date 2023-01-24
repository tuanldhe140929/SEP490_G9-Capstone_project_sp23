import { Injectable } from '@angular/core';
import { AuthResponse } from '../DTOS/AuthResponse';
const AUTH_RESPONSE_KEY = 'auth-user';
const TOKEN_KEY = 'auth-token';
@Injectable({
  providedIn: 'root'
})
export class StorageService {
  constructor() {}

  clearStorage(): void {
    window.localStorage.clear();
  }

	signOut(): void {
    localStorage.clear();
  }

  public saveToken(token: string): void {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.localStorage.getItem(TOKEN_KEY);
  }
  public saveUser(authResponse: AuthResponse): void {
    window.localStorage.removeItem(AUTH_RESPONSE_KEY);
    window.localStorage.setItem(AUTH_RESPONSE_KEY, JSON.stringify(authResponse));
  }

  public getAuthResponse(): any {
    const authResponseJson = window.localStorage.getItem(AUTH_RESPONSE_KEY);
    let authResponse:AuthResponse;
    if (authResponseJson) {
       let authResponse:AuthResponse =  JSON.parse(authResponseJson);
       return authResponse;
    }
    return "";
    
  }

  public isLoggedIn(): boolean {
    const user = window.localStorage.getItem(AUTH_RESPONSE_KEY);
    if (user) {
      return true;
    }
    return false;
  }
}
