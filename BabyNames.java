import java.io.*;
import org.apache.commons.csv.*;
import edu.duke.*;




public class BabyNames {
    
    private final int firstCol = 0;
    private final int secondCol = 1;
    private final int thirdCol = 2;
    private final String male = "M";
    private final String female = "F";
    
    private void totalBirths(FileResource year){
        int noOfBoys = 0, noOfGirls = 0;
        for (CSVRecord record : year.getCSVParser(false)){
            if (record.get(secondCol).equals(female))
                noOfGirls++;
            else
                noOfBoys++;
        }
        System.out.println("No. of Boys - " + noOfBoys);
        System.out.println("No. of Girls - " + noOfGirls);
    }
    
    private int getRank (FileResource year, String name, String gender){
        int countFemale = 0, countItr = 0;
        boolean checkFlag = false;
        
        if (gender.equals(female)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(male))
                    break;
                if(name.equals(record.get(firstCol))){
                    checkFlag = true;
                    countItr++;
                    break;
                }
                countItr++;
            }
            if(checkFlag)
                return countItr;
            else
                return -1;
        }
        else if (gender.equals(male)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(female))
                    countFemale++;
                else{
                    if(name.equals(record.get(firstCol))){
                        checkFlag = true;
                        countItr++;
                        break;
                    }
                }
                countItr++;
            }
            if(checkFlag)
                return countItr - countFemale;
            else
                return -1;
        }
        else
            return -2;
    }
    
    private String getName (FileResource year, int rank, String gender){
        int countRank = 1;
        boolean checkFlag = false;
        
        if (gender.equals(female)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(male))
                    break;
                
                if(countRank == rank){
                    return record.get(firstCol);
                }
                ++countRank;
            }
            return "NOT FOUND";
        }
        else if (gender.equals(male)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(female))
                    continue;
                if(countRank == rank){
                    return record.get(firstCol);
                }
                ++countRank;
            }
            return "NOT FOUND";
        }
        else
            return "NOT FOUND (Invalid Gender)";
        
    }
    
    private void whatIsNameInYear (String name, FileResource year, FileResource newYear, String gender){
        int rank = getRank (year, name, gender);
        String newName = getName (newYear, rank, gender);
        System.out.println("The name " +" would be " + newName  +
                            " as both has rank " + rank);
    }
    
    private String yearOfHighestRank (String name, String gender){
        DirectoryResource dr = new DirectoryResource();
        int rank = 0;
        String nameOfFile = "";
        boolean flag = true;
        for(File f : dr.selectedFiles()){
            FileResource year = new FileResource(f);
            int currentRank = getRank(year, name, gender);
            if (flag && currentRank > 0){
                rank = currentRank;
                nameOfFile = f.getName();
                flag = false;
            }
            if(rank > currentRank && currentRank > 0){
                rank = currentRank;
                nameOfFile = f.getName();
            }
        }
        if (flag)
            nameOfFile = "This Name doesnot exists";
        return nameOfFile;
    }
    
    private float getAverageRank (String name, String gender){
        DirectoryResource dr = new DirectoryResource();
        int counter = 0;
        float avg = 0;
        boolean flag = true;
        for(File f : dr.selectedFiles()){
            FileResource year = new FileResource(f);
            int currentRank = getRank(year, name, gender);
            if(currentRank > 0){
                avg += currentRank;
                ++counter;
            }
        }
        if (avg == 0)
            return 0;
        return avg/counter;
    }
    
    private int getTotalBirthsRankedHigher (FileResource year, String name, String gender){
        int rank = getRank(year, name, gender), noOfBirths = 0, itr = 0;
        
        if(rank == -1)
            return -1;
        if (gender.equals(female)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(male))
                    break;
                itr++;
                noOfBirths += Integer.parseInt(record.get(thirdCol));
                if(itr == rank - 1){
                    return noOfBirths;
                }
            }
            return -1;
        }
        else if (gender.equals(male)){
            for (CSVRecord record : year.getCSVParser(false)){
                if(record.get(secondCol).equals(female))
                    continue;
                itr++;
                noOfBirths += Integer.parseInt(record.get(thirdCol));
                if(itr == rank - 1){
                    return noOfBirths;
                }
            }
            return -1;
        }
        return noOfBirths; 
    }
    
    public void testTotalBirths(){
        FileResource year = new FileResource();
        totalBirths(year);
    }
    
    public void testGetRank(){
        FileResource year = new FileResource();
        String name = "Frank", gender = male;
        int rank = getRank(year, name, gender);
        System.out.println("The rank of " + name + " = " + rank);
    }
    
    public void testGetName(){
        FileResource year = new FileResource();
        String gender = male;
        int rank = 450;
        String name = getName(year, rank, gender);
        System.out.println("The name at " + rank + " for gender " + gender + " = " + name);
    }
    
    public void testWhatIsNameInYear(){
        FileResource year = new FileResource();
        FileResource newYear = new FileResource();
        String name = "Owen", gender = male;
        whatIsNameInYear(name, year, newYear, gender);
    }
    
    public void testYearOFHighestRank(){
        String name = "Mich", gender = male, year = new String();
        year = yearOfHighestRank(name, gender);
        System.out.println("Year of highest rank for " + name + " is " + year);
    }
    
    public void testAverageRank(){
        String name = "Robert", gender = male;
        float avgRank = getAverageRank(name, gender);
        System.out.println("Average rank for " + name + " is " + avgRank);
    }
    
    public void testGetTotalBirthsRankedHigher(){
        FileResource year = new FileResource();
        String name = "Drew", gender = male;
        int count = getTotalBirthsRankedHigher(year, name, gender);
        System.out.println("No. of names above " + name + " is " + count);
    }
}
   
