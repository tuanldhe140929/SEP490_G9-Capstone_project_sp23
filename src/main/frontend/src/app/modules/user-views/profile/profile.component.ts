import { AppComponent } from 'src/app/app.component';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { FormBuilder, Validators } from '@angular/forms';
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

  authResponse: AuthResponse = new AuthResponse();
  user: User = new User();
  constructor(private FormBuilder: FormBuilder,
    private httpClient: HttpClient,
    private app: AppComponent,
    private storageService: StorageService,
    private manageAccountInfoService: ManageAccountInfoService) {

  }
  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
    this.getUserInfo();
    console.log(this.user.avatar);
  }

  public Profileform = this.FormBuilder.group({
    "newFirstName": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    "newUsername": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
    "newLastName": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]]
  });

  get Form() {
    return this.Profileform.controls;
  }
  get newUsername() {
    return this.Profileform.controls.newUsername;
  }
  get newFirstName() {
    return this.Profileform.controls.newFirstName
  }
  get newLastName() {
    return this.Profileform.controls.newLastName;
  }
  public username = "";

  onChangeName() {
    this.Profileform.controls.newUsername.setValue(this.user.username)
    this.Profileform.controls.newFirstName.setValue(this.user.firstName)
    this.Profileform.controls.newLastName.setValue(this.user.lastName)
    this.manageAccountInfoService.onChangeName(this.Profileform.value).subscribe(
      data => {
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }

  getUserInfo() {
    this.manageAccountInfoService.getCurrentUserInfo().subscribe(
      data => {
        this.user = data;
        
        if (this.user.avatar == "" || this.user.avatar == null) {  
            this.ProfileImage.setAttribute('src',"http://ssl.gstatic.com/accounts/ui/avatar_2x.png") ;
        } else {
         this.ProfileImage.setAttribute('src',"http://localhost:9000/public/serveMedia/image?source=" + this.getEncodedUrl(this.user.avatar)) ;
        }
        
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }
  
  getEncodedUrl(source:string){
	  const encodedSource = encodeURIComponent(source.replace(/\\/g, '/').replace('//', '/').replace(/\(/g, '%28').replace(/\)/g, '%29'));
	  return encodedSource;
  }
  UpdateInfo() {
    if (this.UsernameInput != null && this.FirstNameInput != null && this.LastNameInput != null) {
      this.UsernameInput.removeAttribute('readonly');
      this.FirstNameInput.removeAttribute('readonly');
      this.LastNameInput.removeAttribute('readonly');
    }
    console.log("abc")
  }
  get ProfileImage(){
    return document.getElementById('Image') as HTMLImageElement;
  }
  get UsernameInput() {
    return document.getElementById('user_name') as HTMLInputElement;
  }
  get FirstNameInput() {
    return document.getElementById('first_name') as HTMLInputElement;
  }
  get LastNameInput() {
    return document.getElementById('last_name') as HTMLInputElement;
  }

  UploadProfileImage($event: any) {
    const file: File = $event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append("thumbnail", file);
      const upload$ = this.manageAccountInfoService.uploadProfileImage(file).subscribe(
        data => {
          this.user.avatar = data;
          console.log(data);
          this.ProfileImage.setAttribute('src',"" );
          this.ProfileImage.setAttribute('src',"http://localhost:9000/public/serveMedia/serveProfileImage?userId=" + this.user.id);
        },
        error => {
          console.log(error)
        }
      )
    }
  }

}
