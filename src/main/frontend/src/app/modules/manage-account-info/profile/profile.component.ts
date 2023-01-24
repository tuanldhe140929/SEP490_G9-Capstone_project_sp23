import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/DTOS/User';
import { AuthService } from 'src/app/services/auth.service';

import { StorageService } from 'src/app/services/storage.service';
import { ManageAccountInfoService } from '../../../services/manage-account-info.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(private manageAccountInfoService: ManageAccountInfoService, private storageService: StorageService, private authService: AuthService, private router: Router) { }
	
	ngOnInit(): void {
	}
}
