import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthResponse } from 'src/app/DTOS/AuthResponse';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  authResponse: AuthResponse = new AuthResponse();

  constructor(private storageService: StorageService, private router: Router){}

  ngOnInit(): void {
    this.authResponse = this.storageService.getAuthResponse();
  }

  toAllInspectors(){
    this.router.navigate(['inspectors']);
  }
}
