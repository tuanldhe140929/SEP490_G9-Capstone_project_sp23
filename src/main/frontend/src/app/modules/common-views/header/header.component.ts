import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/DTOS/User';
import { AuthService } from 'src/app/services/auth.service';
import { ManageAccountInfoService } from 'src/app/services/manage-account-info.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{
  
  loginStatus: boolean;
  username: string;
  user: User;

  constructor(
    private storageService: StorageService, 
    private authService: AuthService, 
    private manageAccountInfoService: ManageAccountInfoService,
    private router: Router){

  }
  
  ngOnInit(): void {
    if(this.storageService.isLoggedIn()){
      this.loginStatus = true;
      this.manageAccountInfoService.getCurrentUserInfo().subscribe(
        data => {
          this.user = data;
        }
      )
    }else{
      this.loginStatus = false;
    }
  }

  onLogout(){
    this.authService.logout().subscribe(
      data => {
        console.log('Logged out');
        this.router.navigate(['login']);
      }
    )
  }
}
