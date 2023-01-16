import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
	
	registerForm = this.formBuilder.group({
		"email": ['',[Validators.required,Validators.email]],
		"password": ['',[Validators.required,Validators.minLength(5),Validators.maxLength(30)]]
	});
	
	constructor(private http:HttpClient, private formBuilder:FormBuilder, private authService:AuthService){}
	
    ngOnInit(): void {}
    
    public onRegister() {	
		if(this.registerForm.valid){
			this.authService.register(this.registerForm.value).subscribe(
				data => {console.log(data)},
				error => {}
			);
		}
	}
}
