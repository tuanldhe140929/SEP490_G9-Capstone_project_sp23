import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-staff-base',
  templateUrl: './staff-base.component.html',
  styleUrls: ['./staff-base.component.css']
})
export class StaffBaseComponent {

  routerName = 'productApproval';

  constructor(
    private accountService: AccountService,
    private router: Router
  ){}

  logout(){
    this.accountService.logout().subscribe(
      response => {
        console.log("log out successfully");
        this.router.navigate(['login']);
      }
    )
  }

  openProductApproval(){

  }
}
