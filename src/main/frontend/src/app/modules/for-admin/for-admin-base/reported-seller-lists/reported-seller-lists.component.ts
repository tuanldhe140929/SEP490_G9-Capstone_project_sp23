import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Seller } from 'src/app/dtos/Seller';
import { SellerService } from 'src/app/services/seller.service';
import { AddviolationComponent } from '../addviolation/addviolation.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { data } from 'jquery';
import { Account } from 'src/app/dtos/Account';
import { BansellerComponent } from '../banseller/banseller.component';

@Component({
  selector: 'app-reported-seller-lists',
  templateUrl: './reported-seller-lists.component.html',
  styleUrls: ['./reported-seller-lists.component.css']
})
export class ReportedSellerListsComponent implements OnInit{
  displayedColumns: string[] = ['ID','Email', 'Ngày tạo', 'Trạng thái', 'Cấm'];
  dataSource: MatTableDataSource<Seller>;

  account_id: number;

  reportedsellerList: Seller[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  constructor(private sellerService: SellerService, private dialog: MatDialog) {


    // Assign the data to the data source for the table to render
    this.sellerService.getAllSellers().subscribe(
      response => {
        this.dataSource = new MatTableDataSource(response);
        this.reportedsellerList = response;
        // for (let i = 0; i < this.reportedsellerList.length; i++) {
        //   this.account_idList.push(this.reportedsellerList[i].id);
        // }
        this.dataSource.paginator = this.paginator;
        console.log(response)
      }
    )

  }
  ngOnInit(): void {
// this.activateRoute.queryParams.subscribe(data=>{
  console.log(data);
// });
  }

  openViolationDialog(account_id: number) {
    const dialogRef = this.dialog.open(AddviolationComponent, {
      width: '350px',
      data: {
        account_id: account_id,
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }
  openBanSellerDialog(id: number, email: string, enabled: boolean) {
    const dialogRef = this.dialog.open(BansellerComponent, {
      data: {
        id: id,
        email: email,
        enabled: enabled
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      setTimeout(() => this.refresh(),400)
    });
  }

  refresh() {
    this.sellerService.getAllSellers().subscribe((data: any) => {
      this.dataSource.data = data;
      console.log(data);
    })
  }

}
