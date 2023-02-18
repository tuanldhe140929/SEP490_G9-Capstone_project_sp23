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
    console.log(this.user.image);
  }

  public Profileform = this.FormBuilder.group({
    "newname": ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]]
  });

  get Form() {
    return this.Profileform.controls;
  }
  get newname() {
    return this.Profileform.controls.newname;
  }
  public username = "";

  onChangeName() {
    this.Profileform.controls.newname.setValue(this.user.username)
    this.manageAccountInfoService.onChangeName(this.Profileform.value).subscribe(
      data => {
        this.authResponse.username = data.username;
        this.storageService.saveUser(this.authResponse);
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
        console.log(data)
      },
      error => {
        console.log(error)
      }
    )
  }
  UpdateInfo() {
    if (this.UsernameInput != null) {
      this.UsernameInput.removeAttribute('readonly');
    }
    console.log("abc")
  }

  get UsernameInput() {
    return document.getElementById('first_name') as HTMLInputElement;
  }

  UploadProfileImage($event: any) {
    const file: File = $event.target.files[0];

    if (file) {
      const formData = new FormData();

      formData.append("thumbnail", file);

      const upload$ = this.manageAccountInfoService.uploadProfileImage(file).subscribe(
        data => {
          this.user.image = data;
          console.log(data)
        },
        error => {
          console.log(error)
        }
      )
    }
  }
  getUserImage(): string {

    if (this.user.image == "" && this.user.image == null) {
      return "http://ssl.gstatic.com/accounts/ui/avatar_2x.png";
    } else {
      return "http://localhost:9000/public/serveMedia/serveProfileImage?userId=" + this.user.id;
    }
  }
}
