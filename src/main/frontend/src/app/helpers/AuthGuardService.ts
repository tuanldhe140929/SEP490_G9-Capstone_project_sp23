// src/app/auth/auth-guard.service.ts
import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(public authService: AuthService, public router: Router) { }
  canActivate(): boolean {
    //   if (!AuthService.isLoggedIn) {
    //		console.log(AuthService.isLoggedIn);
    //    this.router.navigate(['login']);
    //return false;
    //  }else{
    return true;
  }
}
//}
