import React from "react";

export default function LogoPicbloom() {
  return (
    <svg
      width="180"
      height="50"
      viewBox="0 0 180 50"
      xmlns="http://www.w3.org/2000/svg"
    >
      <text
        x="0"
        y="40"
        fontFamily="'Pacifico', cursive"
        fontSize="40"
        fontWeight="bold"
        fill="url(#paint0_linear)"
      >
        Picbloom
      </text>

      <defs>
        <linearGradient
          id="paint0_linear"
          x1="0"
          y1="0"
          x2="100%"
          y2="0"
          gradientUnits="userSpaceOnUse"
        >
          <stop stopColor="#0d6efd" /> {/* Bootstrap primary */}
          <stop offset="1" stopColor="#084298" /> {/* Darker blue */}
        </linearGradient>
      </defs>
    </svg>
  );
}
