import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-staff-base',
  templateUrl: './staff-base.component.html',
  styleUrls: ['./staff-base.component.css']
})
export class StaffBaseComponent {

  routerName = 'productApproval';

  constructor(
    private authService: AuthService,
    private router: Router
  ){}

  logout(){
    this.authService.logout().subscribe(
      response => {
        console.log("log out successfully");
        this.router.navigate(['login']);
      }
    )
  }

  openProductApproval(){

  }
}
