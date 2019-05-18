package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirect the user to the upload form
       // response.sendRedirect( "Upload.jsp" );
	
        request.getRequestDispatcher("/WEB-INF/view/Upload.jsp").forward(request, response);
	   // request.getRequestDispatcher("/WEB-INF/view/FileUploadResponse.jsp").forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
         // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig()
            .getServletContext();
        File repository = (File) servletContext
            .getAttribute( "javax.servlet.context.tempdir" );
        factory.setRepository( repository );
         // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload( factory );
         // Count how many files are uploaded
        int count = 0;
        // The directory we want to save the uploaded files to.
        String fileDir = getServletContext().getRealPath( "/uploads" );
         // Parse the request
        try
        {
            List<FileItem> items = upload.parseRequest( request );
            for( FileItem item : items )
            {
                // If the item is not a form field - meaning it's an uploaded
                // file, we save it to the target dir
                if( !item.isFormField() )
                {
                    // item.getName() will return the full path of the uploaded
                    // file, e.g. "C:/My Documents/files/test.txt", but we only
                    // want the file name part, which is why we first create a
                    // File object, then use File.getName() to get the file
                    // name.
                    String fileName = (new File( item.getName() )).getName();
                    File file = new File( fileDir, fileName );
                    item.write( file );
                    ++count;
                }
            }
         }
        catch( Exception e )
        {
            throw new IOException( e );
        }
        
         response.setContentType( "text/html" );
        // response.setContentType( "UploadedFiles" );
         
        PrintWriter out = response.getWriter();  //THIS CODE WAS ADDED TO SHOW THE RESULT WITH SOME FORMAT AND THE BUTTON LINK TO BACK HOME
        out.println("<!DOCTYPE html>");
        out.println( "<html lang=\"en\">");
        out.println("	<meta charset=\"UTF-8\">");
		out.println("	<title>Uploading </title>");
		out.println("	<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">");
		out.println("</head>");
        out.println("<body style=\"background-color: powderblue\"}>");
        out.println("<div class=\"container\">");
        out.println("	<h1 style=\"text-align:center\"> Uploading Results </h1>");
       
        
        out.println( "<p style=\"text-align: center\">" + count + " file(s) uploaded to " + fileDir );
        out.println("</br>");
       
        out.println("<div class\"return\">");
        out.println("<p style=\"text-align: center\"><a class=\"btn btn-success\" href=\"Upload\">Return to Upload</a> </p>");
        out.println("</div>");
        out.println("</div>");
        out.println( "</body></html>" );
	}
