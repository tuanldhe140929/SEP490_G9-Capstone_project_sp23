import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { AccountService } from 'src/app/services/account.service';
import { StorageService } from 'src/app/services/storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  @ViewChild('messageModal', { static: false }) private messageModal: any;

  @ViewChild('messageModal2', { static: false }) private messageModal2: any;

  loading = false;
  username = "";
  message = "";

  registerForm = this.formBuilder.group({
    "email": ['', [Validators.required, Validators.email]],
    "username": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(10),
    					this.noWhitespaceValidator]],
    "password": ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]],
    "repeat_password": ['', [Validators.required]]
  });
  
public noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().includes(' ');
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
}

  onPasswordMatch() {

    if (this.confirm_password.value == this.password.value) {
      return true;
    } else {
      return false;
    }
  }

  get Form() {
    return this.registerForm.controls;
  }
  get password(): AbstractControl {
    return this.registerForm.controls['password'];
  }

  get confirm_password(): AbstractControl {
    return this.registerForm.controls['repeat_password'];
  }

  public redirectLogin() {
    this.router.navigate(['login']);
  }

  public onUsernameChange($event: any) {
	  if($event.keyCode===32){
		  $event.preventDefault();
	  }
	  this.username = this.username.toLowerCase();
    this.registerForm.controls.username.setValue(this.username);
  }

  dismissError() {
    this.message = "";
    this.redirectLogin();
    this.modalService.dismissAll();
  }

  dismissError2() {
    this.message = "";
    this.modalService.dismissAll();
  }

  constructor(private http: HttpClient, private formBuilder: FormBuilder,
   private userService: UserService,
    private router: Router, private storageService: StorageService,
    private modalService: NgbModal  ) { }

  ngOnInit(): void { }

  public onRegister() {
    this.loading = true;
    this.registerForm.controls.username.setValue(this.username);
    if (this.registerForm.valid) {
      
      this.userService.register(this.registerForm.value).subscribe(
        data => {
          console.log(data);
          this.loading = false;
          this.message = "Đăng kí thành công, kiểm tra email để xác thực";
          this.storageService.saveRegisteredEmail(data);
          this.openModal();
          //this.sendVerifyEmail(data.email);
        },
        error => {
          console.log(error);
          this.loading = false;
          if (error.status === 409) {
            if (error.error.messages[0].includes(this.registerForm.controls.username.value)) {
              this.message = "Tên người dùng đã tòn tại";
            } else {
              this.message = "Email đã được đăng kí"
            }
            this.openModal2();
          } else {
            this.message = "Đăng kí thành công, không thể gửi mail xác thực";
            this.openModal2;
          }
        }
      );
    }
  }

  openModal() {
    this.modalService.open(this.messageModal, { centered: true });
  }

  openModal2() {
    this.modalService.open(this.messageModal2, { centered: true });
  }
  
  public sendVerifyEmail(email:string){
	  this.userService.sendVerifyEmail(email).subscribe(
		  (data)=>{
			  console.log(data);
		  },
		  (error)=>{
			  console.log(error);
		  }
	  )
  }
}
