package com.nitrr.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nitrr.model.Patient;
import com.nitrr.model.dto.LivePatientsDTO;

/**
 * Servlet implementation class BaseController
 */
@WebServlet({"/BaseController","/live/patient"})
public class BaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		Gson gson = new Gson();
		String json = gson.toJson(getAllLivePatient());
		response.getWriter().write(json);
		response.setStatus(HttpServletResponse.SC_OK);
		
	}
	protected LivePatientsDTO getAllLivePatient() {
		return new LivePatientsDTO(System.currentTimeMillis(), AppMainEndPoint.getAllLive());
	}
	

}
