import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {
  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
    private storageService: StorageService, private router: Router) {
		
	}
    ngOnInit(): void {

let email = this.storageService.getRegisteredEmail();
if(email!=null){
       var verifyLink = this.activatedRoute.snapshot.paramMap.get('verifyLink');
       this.authService.verifyEmail(verifyLink,email).subscribe(
		   (data)=>{
           if (data == true) {
             this.router.navigate(['login']);
           }
		   },
		   (error)=>{
         this.router.navigate(['error']);
		   }
	   )
    }
}
}
