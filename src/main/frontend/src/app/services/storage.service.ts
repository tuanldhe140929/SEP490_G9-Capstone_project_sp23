import { Injectable } from '@angular/core';
import { AuthResponse } from '../dtos/AuthResponse';
const AUTH_RESPONSE_KEY = 'auth-user';
const TOKEN_KEY = 'auth-token';
const REGISTERED_EMAIL_KEY = 'registered-email'
@Injectable({
  providedIn: 'root'
})
export class StorageService {
    saveRegisteredEmail(email: string) {
       window.localStorage.setItem(REGISTERED_EMAIL_KEY,email);
    }
    getRegisteredEmail():string | null {
       return window.localStorage.getItem(REGISTERED_EMAIL_KEY);
    }
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
    if (authResponseJson) {
       let authResponse:AuthResponse =  JSON.parse(authResponseJson);
       return authResponse;
    }
    return null;
    
  }

  public isLoggedIn(): boolean {
    const user = window.localStorage.getItem(AUTH_RESPONSE_KEY);
    if (user) {
      return true;
    }
    return false;
  }
}
