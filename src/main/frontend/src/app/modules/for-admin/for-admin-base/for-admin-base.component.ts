import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-for-admin-base',
  templateUrl: './for-admin-base.component.html',
  styleUrls: ['./for-admin-base.component.css']
})
export class ForAdminBaseComponent {
  constructor(private router: Router, private accountService: AccountService){}

  routerName: string = 'staffs';

  dashboardChosen: boolean = true;
  manageStaffChosen: boolean = false;
  categoriesChosen: boolean = false;
  tagsChosen: boolean = false;
  reportedSellerChosen: boolean = false;

  // openDashboard(){
  //   this.routerName = 'dashboard';
  //   this.dashboardChosen = true;
  //   this.manageStaffChosen = false;
  //   this.categoriesChosen = false;
  //   this.tagsChosen = false;
  //   this.reportedSellerChosen = false;
  // }

  openManageStaffs(){
    this.routerName = 'staffs';
    this.dashboardChosen = false;
    this.manageStaffChosen = true;
    this.categoriesChosen = false;
    this.tagsChosen = false;
    this.reportedSellerChosen = false;
  }

  // openCategories(){
  //   this.routerName = 'categories';
  //   this.dashboardChosen = false;
  //   this.manageStaffChosen = false;
  //   this.categoriesChosen = true;
  //   this.tagsChosen = false;
  //   this.reportedSellerChosen = false;

  // }

  // openTags(){
  //   this.routerName = 'tags';
  //   this.dashboardChosen = false;
  //   this.manageStaffChosen = false;
  //   this.categoriesChosen = false;
  //   this.tagsChosen = true;
  //   this.reportedSellerChosen = false;
  // }

  logout(){
    this.accountService.logout().subscribe(
      response => {
        console.log("log out successfully");
        this.router.navigate(['login']);
      }
    )
  }

  openReportedseller(){
    this.routerName = 'reportedsellerlist';
    this.dashboardChosen = false;
    this.manageStaffChosen = false;
    this.categoriesChosen = false;
    this.tagsChosen = false;
    this.reportedSellerChosen = true;
  }

}
