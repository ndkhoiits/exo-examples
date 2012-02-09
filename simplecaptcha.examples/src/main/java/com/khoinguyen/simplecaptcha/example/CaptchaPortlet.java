/**
 * Copyright (C) 2012 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.khoinguyen.simplecaptcha.example;

import static nl.captcha.Captcha.NAME;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

/**
 * @author <a href="mailto:ndkhoi168@gmail.com">Nguyen Duc Khoi</a>
 * Feb 9, 2012
 */
public class CaptchaPortlet extends GenericPortlet
{
   protected int _width = 200;

   protected int _height = 50;

   @Override
   protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
   {
      ResourceURL resourceURL = response.createResourceURL();
      String random = "&amp;v=" + Calendar.getInstance().getTimeInMillis();
      PortletURL refreshActionURL = response.createActionURL();
      String imgURL = resourceURL.toString() + random;
      refreshActionURL.setParameter(ActionRequest.ACTION_NAME, "refresh");

      request.setAttribute("refreshURL", refreshActionURL);
      request.setAttribute("imgURL", imgURL);

      Boolean status = (Boolean) request.getPortletSession().getAttribute("STATUS");
      if (status != null)
      {
         request.setAttribute("status", status);
      }

      getPortletContext().getRequestDispatcher("/index.jsp").forward(request, response);
   }

   @ProcessAction(name = "refresh")
   public void refreshAction(ActionRequest request, ActionResponse response)
   {
      PortletSession session = request.getPortletSession();
      Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
      session.setAttribute("STATUS", (captcha != null && captcha.isCorrect(request.getParameter("answer"))));
      session.removeAttribute(NAME);
   }

   @Override
   public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException
   {
      PortletSession session = request.getPortletSession();
      Captcha captcha;
      if (session.getAttribute(NAME) == null)
      {
         captcha = new Captcha.Builder(_width, _height).addText().gimp().addNoise().addBackground().build();

         session.setAttribute(NAME, captcha);
         writeImage(response, captcha.getImage());

         return;
      }
   }

   private static void writeImage(ResourceResponse response, BufferedImage bi)
   {
      response.setProperty("Cache-Control", "private,no-cache,no-store");
      response.setContentType("image/png"); // PNGs allow for transparency. JPGs do not.
      try
      {
         CaptchaServletUtil.writeImage(response.getPortletOutputStream(), bi);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
