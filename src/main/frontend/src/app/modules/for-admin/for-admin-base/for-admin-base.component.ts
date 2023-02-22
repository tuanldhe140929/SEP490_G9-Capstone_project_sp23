import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-for-admin-base',
  templateUrl: './for-admin-base.component.html',
  styleUrls: ['./for-admin-base.component.css']
})
export class ForAdminBaseComponent {
  constructor(private router: Router){}

  routerName: string = 'dashboard';

  openDashboard(){
    this.routerName = 'dashboard';
  }

  openManageStaffs(){
    this.routerName = 'staffs';
  }

  openCategories(){
    this.routerName = 'categories';
  }

  openTags(){
    this.routerName = 'tags';
  }
}
