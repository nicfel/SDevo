# SDevo


SDevo is a [BEAST2](http://beast2.org) package that implementes the multi-type birth death process with state dependent rates of evolution. It provides the BEAST2 code for the manuscript **State-dependent evolutionary models reveal modes of solid tumor growth** by Lewinsohn et al. The material for this manuscript can be found [here](https://github.com/blab/spatial-tumor-phylodynamics).

SDevo depends on [BDMM-prime](https://github.com/tgvaughan/BDMM-Prime), a re-implementation of the BDMM package for BEAST2. 


# Installation

To install SDevo, you need to download BEAST v.2.6.x (SDevo is not yet compatible with version 2.7).
After installing beast, open BEAUti and go to File >> Manage Packages. Next, click on `package repositories`.
In the window that opens up, click on `add URL` and add the following URL
 `https://raw.githubusercontent.com/nicfel/SDevo/main/packages.xml`. This will tell BEAUti where to find SDevo and BDMM-prime (upon which SDevo depends) and will allow you to install the package.
 After installing both packages, you can restart BEAUti and setup a BDMM prime analysis with either Canonical or Epi Parameterization (FBD, as well as birth rates across demes are currently untested in SDevo).
 Then, you can go back to the clock model part and select `State dependent clock rates`. 


# License

The content of this project itself is licensed under the Creative Commons Attribution 3.0 license, and the java source code of esco is licensed under the GNU General Public License.
