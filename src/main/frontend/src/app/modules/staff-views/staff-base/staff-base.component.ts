import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-staff-base',
  templateUrl: './staff-base.component.html',
  styleUrls: ['./staff-base.component.css']
})
export class StaffBaseComponent {

  productApprovalChosen: boolean = true;
  reportListChosen: boolean = false;

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
    this.routerName = 'productApproval'
    this.productApprovalChosen = true;
    this.reportListChosen = false;
  }

  openReportList(){
    this.routerName = 'reportList'
    this.productApprovalChosen = false;
    this.reportListChosen = true;
  }
  
}
