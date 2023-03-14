import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';

@Component({
  selector: 'app-login-with-google',
  templateUrl: './login-with-google.component.html',
  styleUrls: ['./login-with-google.component.css']
})
export class LoginWithGoogleComponent implements OnInit {


  constructor(private storageService: StorageService, private route: ActivatedRoute, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    var code = this.route.snapshot.queryParamMap.get('code');
    console.log(code);
    /*if (code != null) {
      this.authService.loginWithGoogle(code).subscribe(
        (response) => {
          this.router.navigate(['login']);
          this.storageService.saveUser(response.body);
          this.storageService.saveToken(response.body.accessToken);
        }
        , error => {
          console.log(error);
        }
      );
    }*/
  }
}
