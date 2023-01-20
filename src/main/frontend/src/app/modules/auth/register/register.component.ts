import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
	
  message = "";

	registerForm = this.formBuilder.group({
		"email": ['',[Validators.required,Validators.email]],
    "password": ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
    "confirm_password": ['', [Validators.required]]
  });

  onPasswordChange() {

    if (this.confirm_password.value == this.password.value) {
      this.confirm_password.setErrors(null);
    } else {
      this.confirm_password.setErrors({ mismatch: true });
    }
  }

  get password(): AbstractControl {
    return this.registerForm.controls['password'];
  }

  get confirm_password(): AbstractControl {
    return this.registerForm.controls['confirm_password'];
  }

  public redirectLogin() {
    this.router.navigate(['login']);
  }
 
	
  constructor(private http: HttpClient, private formBuilder: FormBuilder, private authService: AuthService,
    private router: Router) { }
	
    ngOnInit(): void {}
    
    public onRegister() {	
		if(this.registerForm.valid){
			this.authService.register(this.registerForm.value).subscribe(
				data => {console.log(data)
				this.message = "Dang ki thanh cong"},
				error => {
					if(error.status===400){
						this.message = "Trung ten dang nhap";
					}
				}
			);
		}
	}
}
