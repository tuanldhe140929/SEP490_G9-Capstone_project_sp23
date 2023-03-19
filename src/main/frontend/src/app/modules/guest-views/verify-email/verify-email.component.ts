import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {
	constructor(private userService: UserService,private activatedRoute: ActivatedRoute,
	private storageService:StorageService){
		
	}
    ngOnInit(): void {

let email = this.storageService.getRegisteredEmail();
if(email!=null){
       var verifyLink = this.activatedRoute.snapshot.paramMap.get('verifyLink');
       this.userService.verifyEmail(verifyLink,email).subscribe(
		   (data)=>{
			   console.log(data);
		   },
		   (error)=>{
			   console.log(error);
		   }
	   )
    }
}
}
