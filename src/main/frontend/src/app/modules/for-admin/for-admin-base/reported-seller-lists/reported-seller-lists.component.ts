import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Seller } from 'src/app/dtos/Seller';
import { SellerService } from 'src/app/services/seller.service';
import { AddviolationComponent } from '../addviolation/addviolation.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-reported-seller-lists',
  templateUrl: './reported-seller-lists.component.html',
  styleUrls: ['./reported-seller-lists.component.css']
})
export class ReportedSellerListsComponent implements AfterViewInit{
  displayedColumns: string[] = ['Email', 'Ngày tạo', 'Cấm'];
  dataSource: MatTableDataSource<Seller>;

  reportedsellerList: Seller[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  constructor(private sellerService: SellerService, private dialog: MatDialog) {

    // Assign the data to the data source for the table to render
    this.sellerService.getAllSellers().subscribe(
      response => {
        this.dataSource = new MatTableDataSource(response);
        this.reportedsellerList = response;
        this.dataSource.paginator = this.paginator;
        console.log(response)
      }
    )

  }

  openViolationDialog() {
    const dialogRef = this.dialog.open(AddviolationComponent, {
      width: '350px',
     
    });

    // dialogRef.afterClosed().subscribe(result => {
    //   console.log(`Dialog result: ${result}`);
    //   setTimeout(() => this.refresh(),400)
    // });
  }

  ngAfterViewInit() {

  }

  refresh() {
    this.sellerService.getAllSellers().subscribe((data: any) => {
      this.dataSource.data = data;
      console.log(data);
    })
  }

}
