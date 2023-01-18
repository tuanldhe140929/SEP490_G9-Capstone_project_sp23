import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/User';
import { AuthService } from 'src/app/services/auth.service';

import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
    
	 user: User | undefined;
	
	
	constructor(private userService:UserService, private storageService:StorageService, private authService:AuthService,private router:Router){}
	
	ngOnInit(): void {
		console.log(this.storageService.getAuthResponse());
        this.userService.getInfo(this.storageService.getAuthResponse().email).subscribe(
			(data: User) => this.user = data 
			
    	)}
    	public logOut(){
		console.log('loginout');
		this.authService.logout().subscribe();
		this.router.navigate(['login']);
	}
}
