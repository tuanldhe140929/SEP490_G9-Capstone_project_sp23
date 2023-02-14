import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {
	constructor(private authService: AuthService,private activatedRoute: ActivatedRoute,
	private storageService:StorageService){
		
	}
    ngOnInit(): void {

let email = this.storageService.getRegisteredEmail();
if(email!=null){
       var verifyLink = this.activatedRoute.snapshot.paramMap.get('verifyLink');
       this.authService.verifyEmail(verifyLink,email).subscribe(
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
