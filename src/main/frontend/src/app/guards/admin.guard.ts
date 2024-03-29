import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthResponse } from '../dtos/AuthResponse';
import { StorageService } from '../services/storage.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private storageService: StorageService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let authResponse: AuthResponse;
    authResponse = this.storageService.getAuthResponse();
    if(authResponse){
      if(authResponse.roles.includes('ROLE_ADMIN')){
        return true;
      }else{
        this.router.navigate(['unauthorized']);
      }
    }
    return false;
  }
  
}
