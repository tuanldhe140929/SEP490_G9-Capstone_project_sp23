import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-for-admin-base',
  templateUrl: './for-admin-base.component.html',
  styleUrls: ['./for-admin-base.component.css']
})
export class ForAdminBaseComponent {
  constructor(private router: Router, private authService: AuthService){}

  routerName: string = 'dashboard';

  dashboardChosen: boolean = true;
  manageStaffChosen: boolean = false;
  categoriesChosen: boolean = false;
  tagsChosen: boolean = false;

  openDashboard(){
    this.routerName = 'dashboard';
    this.dashboardChosen = true;
    this.manageStaffChosen = false;
    this.categoriesChosen = false;
    this.tagsChosen = false;
  }

  openManageStaffs(){
    this.routerName = 'staffs';
    this.dashboardChosen = false;
    this.manageStaffChosen = true;
    this.categoriesChosen = false;
    this.tagsChosen = false;
  }

  openCategories(){
    this.routerName = 'categories';
    this.dashboardChosen = false;
    this.manageStaffChosen = false;
    this.categoriesChosen = true;
    this.tagsChosen = false;
  }

  openTags(){
    this.routerName = 'tags';
    this.dashboardChosen = false;
    this.manageStaffChosen = false;
    this.categoriesChosen = false;
    this.tagsChosen = true;
  }

  logout(){
    this.authService.logout().subscribe(
      response => {
        console.log("log out successfully");
        this.router.navigate(['login']);
      }
    )
  }
}
